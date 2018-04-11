package im

import akka.actor.Actor
import org.bson.types.ObjectId

sealed trait Message

// 保存消息成功后，返回消息
case class SaveMsgSuccess(appId: Int, traceId: Long, channelId: String, msgId: String) extends Message

// 保存消息失败后，返回错误信息，也可以定义错误码，可以根据错误码，接收者可以做进一步操作
case class SaveMsgFailure(appId: Int, traceId: Long, channelId: String, msgId: String, errorMsg: String) extends Message

// 保存回执消息的未读人到MongoDB，这里想做成DBRef的，也即：关联关系。。。
// 最后的一个objId是保存成功的ID，我觉得，这里在客户端直接New一个，然后再存入。
case class SaveUnreadPeopleSuccess(appId: Int, traceId: Long, channelId: String, msgId: String, objId: ObjectId) extends Message

// 保存回执消息未读人失败。
case class SaveUnreadPeopleFailure(appId: Int, traceId: Long, channelId: String, msgId: String, errorMsg: String) extends Message

// 传递一个保存未读人请求，然后一个Actor进行处理，这里还需要再分吗（根据频道的分表情况进行划分）？？？
case class SaveUnreadPeopleRequest(appId: Int, traceId: Long, channelId: String, msgId: String) extends Message

// 进行请求分发，最后一个是存放成员的表。传递一个MongoDB的objID，这个是每个Actor获得的要保存的表。可以有多个Actor
case class SaveUnreadPeopleRequestDispatcher(appId: Int, traceId: Long, channelId: String, msgId: String, objId: ObjectId, memberTables: String*) extends Message




/**************************************上报信息设计*********************************/

/**
  * 设计原则：
  * 1.各个服务器上报自己可以解决的CMD，顺带自己需要请求的dispatcher地址
  * 2.注册拉取Gateway时，需要提供一个Topic，这个topic可以让消费者更新Gateway信息
  * 3.统一上报，统一拉取，然后统一下发。这里可以设计成Singleton
  *
  */


/**
  * 注册上报信息
  * @param dispatcherInfo 调度服务器信息
  * @param ip             服務器IP
  * @param port           服務器端口
  * @param cmd            服務器提供哪些功能
  * @param serverType     服務器類型
  * @param requestType    請求類型
  */
case class RegistryReportInfo(
                               dispatcherInfo: Seq[String],
                               ip: String,
                               port: Int,
                               cmd: Seq[Short],
                               serverType: String,
                               requestType: String
                             )

/**
  * 注册拉取Gateway信息
  * @param dispatcherInfo    调度服务器信息
  * @param requestType       请求类型
  * @param reqServerType     服务器类型
  * @param topic             Pub/Sub的topic，这可以让各个role是sender的actor进行Gateway的更新
  */
case class RegistryPullGatewayInfo(
                                  dispatcherInfo: Seq[String],
                                  requestType: Int,
                                  reqServerType: Int,
                                  topic: String
                                  )

/**
  * 上报服务
  */
trait RegistryReportInfoService {
  this: Actor =>

}

/**************************************消息设计*********************************/

/**
  * 频道服务功能：
  * 1、频道聊天(这个消息跟单聊群聊一样)
  *   1.文本消息
  *   2.语音
  *   3.图片
  *   4.视频
  *   5.位置
  *   6.附件
  *   7.名片
  *   8.频道公告
  *   9.透传消息
  *   10.特殊消息
  *   11.隐藏消息，不计数消息。
  *   12.特定用户收到此条消息
  *   13.@某人或者某些人或者所有人的消息。
  * 2、上报/提交readSeqId
  * 3、删除消息
  * 4、编辑消息
  * 5、消息已读上报
  * 6、点赞消息
  * 7、MQ消息：
  *   1.频道加人、踢人
  *   2.频道透传其它消息，分持久化与否类型。
  *   3.终极Boss发送的MQ消息处理。
  *
  */

/**
  * 频道目前的困难是：频道成员大小不固定，所以，在大成员的频道中，
  * 怎样快速的把消息通知到频道各个成员，以及能够尽量使JVM不发生Full GC.作出一些设计
  *
  */

/**
  * 频道信息中，不设合并操作，因为合并时，还需要变更的信息太多。
  * 对应的Redis是: appId_channelId_channel_info
  * @param channelId        频道ID
  * @param silence          是否禁言
  * @param archived         是否归档
  * @param maxSeq           最大消息SeqID
  * @param lastMsgTime      最后发送时间
  * @param isEnable         是否停用
  * @param count            频道总人数
  * @param number           分表序号
  * @param countThreshold   一个分表中，有多少人
  * @param reduceThreshold  减少到多少人后，成为优先插入队列
  * @param priorityInsert   优先插入队列集合名称
  */
  case class ChannelInfo(
                        channelId: String,
                        silence: Boolean,
                        archived: Boolean,
                        maxSeq: Long,
                        lastMsgTime: Long,
                        isEnable: Boolean,
                        count: Int,
                        number: Int,
                        countThreshold: Int,
                        reduceThreshold: Int,
                        priorityInsert: String
                        )

/**
  * 频道成员
  * 对应的Redis Key是: appId_channelId_members_number  这个Number对应的是频道中的分表序号
  * @param channelId    频道ID
  * @param uid          成员ID
  * @param isMsgBlock   是否接收消息
  * @param others       其它信息(这个，可以再扩展)。
  */
  case class ChannelMembers(
                           channelId: String,
                           uid: Long,
                           isMsgBlock: Boolean,
                           others: Any
                           )

/**
  * 成员对应的频道信息、包括：已读SeqID，MaxSeqID，置顶和免打扰，以及以后可能有的信息。
  *
  * 每个成员单独记录一个频道的最大消息ID，可以解决 发送的消息是特指消息，以及隐藏不计数消息
  *
  * 对应的Redis结构是：Hash结构，对应一个人所有的频道的个人信息。
  * Key: appId_uid_channel
  * Value:
  *       channelId:number,            分表信息
  *       channelId_Top: long          置顶时间
  *       channelId_MsgBlock: Boolean  免打扰
  *       channelId_ReadSeq: Int       已读消息seq
  *       channelId_MaxSeq: Int        频道最大Seq，这个设计成自增，也即，假设是自己的计数消息，则+1、否则不加。
  *       channelId_SpecialCount: Int  频道中特殊消息数
  *
  * @param channelId    频道ID
  * @param number       频道对应的分表序号
  * @param channelInfo  自己设置的频道信息：比如免打扰等，置顶。这里的channel是每个频道的ID组成
  */
  case class User2ChannelInfo(
                             channelId: String,
                             number: Int,
                             channelInfo: String
                             )

/**
  * 优先插入频道分表信息
  * 对应的Redis结构是：可以设计成一个Sorted Set结构。
  * 如果要减少redis的Key数量，可以使用Hash结构，表示所有的频道需要优先插入的分表。
  *
  * Key： appId_channelId_priority_insert
  * Value: number
  * @param channelId 频道ID
  * @param numbers   分表集合
  */
  case class PriorityInsertMember(
                                 channelId: String,
                                 numbers: Seq[Int]
                                 )

/**
  * 目前，以上的变更，不会影响现有阶段消息的不可读等情况。
  *
  * 是否需要提供一个超级管理员的接口？？？？
  *
  */


/**
  * 服务管理中心，所有的服务启动后，都向管理中心注册，然后，定时发送自己的状态，CPU利用率，JVM内存等，也可以发送GC情况。
  *
  * 可以下发命令，关闭服务等等。
  * 可以根据Cluster中，node的状态，去下发down命令，把一个node从Cluster中移除。这个 需要一定的逻辑来判断unreachable
  * 的node。然后下发down。目前可以根据投票原则，假设，有多个node同时上报了某一个node是unreachable的，则移除这个node。
  *
  */


/**
  * 消息派发中心：
  *   1.
  *     1、根据各个sender发送来的CPU、内存等情况，决定优先往哪个sender派发消息。
  *     2、假设一个频道有多个分表情况，则判断分表中的人数，然后再决定往每个sender派发消息。
  *     3、假设有的分表人数较少，少于一定的阈值，则进行多个分表的合并，，然后再进行消息派发。
  *     4、这期间有一个问题：派发前，Sender的情况良好，很有可能会把多个消息同时派发给这个sender，
  *       导致这个sender会瞬间的忙起来，也许消息也会及时的发送，需要测试。
  *     5、假设有多个派发中心，而多个派发中心会同时认为某个sender情况良好，就也会出现sender压力突然增大。
  *        这里所谓的压力增大，会把消息积压到Actor的mailbox中，也即可能会不及时的消费。
  *     6、尽可能快的处理来的消息，避免消息积压。
  *     7、存在消息顺序问题：：：假设设置频道禁言，但是，在派发中，有的普通消息还没有发送到某个成员，
  *        而成员先收到了禁言消息，然后又收到了普通消息，该怎样处理。
  *   2.
  *     1、使用Hash分配策略，同样的频道，发送到相同的senders上，这个，要根据怎么构建频道分表的hash值来决定。
  *       这样，可以保证同一个频道中消息到达每个成员的先后顺序一致性。
  */

/**
  * 消息保存处理：保存MongoDB，Redis
  *   1、发送的消息持久化到MongoDB中。
  *   2、保存特殊消息到MongoDB，  回执消息，@消息。
  *   3、保存每个成员的MaxSeqID。
  *   4、保存特殊消息计数
  *   5、保存回执消息的所有未读人信息。
  *   6、更改频道中最后一条消息时间，(这个在判断完频道状态后，就更改，以及增加频道的最大MaxSeq)，保证消息的可靠性。
  *
  */

/**
  * 接收消息：
  *   1、Netty接收到消息是，进行消息判断。
  *     1.判断消息类型，普通消息(包括：文本、图片、位置、@某人、通告等)，或者是回执消息或者是上报已读，特殊消息上报已读等
  *       1、这些分为  业务逻辑处理
  *     2.简单的消息判断，也即：发送人是否属于频道成员，频道状态等。
  *     3.判断通过，则发送消息到业务逻辑模块处理。
  *   2、MQ接收到消息，包括  透传消息。加人、踢人操作。
  *
  */

/**
  * 简单业务逻辑处理：
  *   1、根据消息类型的不同，可以判断是否需要保存。对seq id进行加一等等操作。
  *   2、回执发送者，消息发送成功。
  *   3、调用dispatcher，进行消息派发，做进一步处理。
  *     1.这里，可以根据消息类型进行进一步封装，也即：直接发送消息，需要处理业务逻辑类，也即：@某人，回执等等。
  */


/**
  * 进一步业务处理：
  *   1、遍历自己分到的分表成员信息，然后做进一步处理。
  *   2、假设，只发送到某一个人，则稍微处理，即可发送出去。
  */






































