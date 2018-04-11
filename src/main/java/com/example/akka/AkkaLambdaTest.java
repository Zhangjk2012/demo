package com.example.akka;

import akka.actor.*;
import akka.japi.Function;
import akka.japi.pf.DeciderBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by ZJK on 2017/9/19.
 */
public class AkkaLambdaTest {

    public static void main(String[] args) {
        final Config config = ConfigFactory.load("application2.conf");
        final ActorSystem actorSystem = ActorSystem.create("AkkaLambdaTest",config);
        actorSystem.actorOf(Props.create(ParentActor.class),"parentActor");
    }
}

class ParentActor extends AbstractActor {
    ActorRef studentActor = getContext().actorOf(Props.create(StudentActor.class),"studentActor");
    private SupervisorStrategy strategy = new OneForOneStrategy(2, Duration.create("1 minute"),
        new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                System.out.println("ParentActor   Catch exception      =====:"+t.getMessage());
                if (t instanceof ArithmeticException) {
                    // 继续工作
                    return SupervisorStrategy.resume();
                } else if (t instanceof NullPointerException) {
                    // 重启
                    return SupervisorStrategy.restart();
                } else if (t instanceof IllegalArgumentException) {
                    // 停止
                    return SupervisorStrategy.stop();
                } else {
                    // 处理不了：
                    if (t instanceof RuntimeException) {
                        System.out.println("sssssssssssssss==============="+ sender());
                    }
                    return  SupervisorStrategy.restart();
                }
            }
        }
    );

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, System.out::println)
                .match(RuntimeException.class, obj -> System.out.println("..........."+obj))
                .build();
    }

}

class StudentActor extends AbstractActor {
    private Cancellable cancellable = null;
    private ActorRef teacherRef = null;

    @Override
    public void preStart() throws Exception {
        teacherRef = getContext().actorOf(Props.create(TeacherActor.class),"teacherActor");
        {
            FiniteDuration initialDelay = Duration.create(1, TimeUnit.SECONDS);
            QuoteRequest request = new QuoteRequest();
            getContext().watch(teacherRef);
//            cancellable = getContext().system().scheduler().schedule(Duration.create(3, TimeUnit.SECONDS), initialDelay, teacherRef, request, getContext().dispatcher(), getSelf());
//            getContext().system().scheduler().scheduleOnce(Duration.create(15, TimeUnit.SECONDS), teacherRef, PoisonPill.getInstance(), getContext().dispatcher(), getSelf());

            getContext().system().scheduler().scheduleOnce(Duration.create(1, TimeUnit.SECONDS), teacherRef, request, getContext().dispatcher(), getSelf());
        }
    }

    // 参数意思是：如果在1分钟内重启次数超过2次，就停掉此actor。
    private static SupervisorStrategy strategy = new OneForOneStrategy(2, Duration.create("1 minute"),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                    System.out.println("Catch exception=====:"+t.getMessage());
                    if (t instanceof ArithmeticException) {
                        // 继续工作
                        return SupervisorStrategy.resume();
                    } else if (t instanceof NullPointerException) {
                        // 重启
                        return SupervisorStrategy.restart();
                    } else if (t instanceof IllegalArgumentException) {
                        // 停止
                        return SupervisorStrategy.stop();
                    } else {
                        // 上报
                        return SupervisorStrategy.escalate();
                    }
                }
            }
    );

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
    }

    private static SupervisorStrategy strategyLambda =
            new OneForOneStrategy(10, Duration.create(1, TimeUnit.MINUTES),
                    DeciderBuilder.match(ArithmeticException.class, e -> resume())
                            .match(NullPointerException.class, e -> restart())
                            .match(IllegalArgumentException.class, e -> stop())
                            .matchAny(o -> escalate()).build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(QuoteResponse.class, this::println)
                .match(String.class, System.out::println)
                .matchUnchecked(List.class, (List<Integer> l) -> {
                    for (Integer i: l) {
                        System.out.println(i);
                    }
                })
                .match(Terminated.class, t->{
                    ActorRef actorRef =  t.getActor();
                    if(actorRef.path().name().equals("teacherActor")){
                        System.out.println("Stop the scheduler");
                        cancellable.cancel();
                    }
                })
                .build();
    }

    @Override
    public void unhandled(Object message) {
        super.unhandled(message);
    }

    private void println(QuoteResponse response) {
        String retStr = response.getQuoteString();
        System.out.println("Receive the response msg is :"+retStr);
    }
}

class QuoteRequest{}
class QuoteResponse{
    public String getQuoteString() {
        return quoteString;
    }

    final private String quoteString;

    public QuoteResponse(String quoteString) {
        this.quoteString = quoteString;
    }
}

class TeacherActor extends AbstractActor {

    private List<String> quotes = new ArrayList<>(4);

    private static int count = 0;

    {
        System.out.println("=========================++++++++++++++++++++++++++++++");
        quotes.add("Moderation is for cowards");
        quotes.add("Anything worth doing is worth overdoing");
        quotes.add("The trouble is you think you have time");
        quotes.add("You never gonna know if you never even try");
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("TeacherActor preStart()=====");
    }

    @Override
    public void postStop() throws Exception {
        getContext().getParent().tell("Stop!!",getSelf());
        System.out.println("TeacherActor postStop()-----");
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        System.out.println("TeacherActor preRestart()-----");
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        System.out.println("TeacherActor postRestart()-----");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(QuoteRequest.class, request -> {
//                    if (count == 5) {
//                        count++;
//                        //抛出一个ArithmeticException
//                        throw new ArithmeticException("Throw ArithmeticException.");
//                    }
//                    if (count == 10) {
//                        count++;
//                        throw new NullPointerException("Throw NullPointerException.");
//                    }
//                    if (count == 15) {
//                        count ++;
//                        throw new RuntimeException("Runtime exception.");
//                    }
//                    if (count  == 30) {
//                        throw new IllegalArgumentException("Throw IllegalArgumentException.");
//                    }
//                    if (count == 5) {
//                        getContext().stop(getSelf());
//                    }
//                    count++;
//                    Random random = new Random();
//                    int index = random.nextInt(4);
//                    String retStr = quotes.get(index);
//                    sender().tell(new QuoteResponse(retStr), self());
                    sender().tell(Arrays.asList("1","zhang", "3"), self());
                })
                .build();
    }
}