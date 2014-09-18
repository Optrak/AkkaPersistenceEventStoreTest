package com.optrak.eventstore

import akka.actor.SupervisorStrategy.{Restart, Stop}
import com.optrak.eventstore.serializers._
import com.optrak.eventstore.actors._
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable._
import org.specs2.time.NoTimeConversions
import akka.actor.{ ActorSystem, Props, Actor }
import akka.serialization._
import eventstore._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object EventStoreTest extends Specification with LazyLogging with NoTimeConversions {
  val system = ActorSystem("test")

  "EventStore" should {
    "store and retrieve the event" in {
      val testEvent = ItemPushed("Hi")

      // Get the Serialization Extension
      val serialization = SerializationExtension(system)

      // Find the Serializer for it
      val serializer = serialization.findSerializerFor(testEvent)

      // Turn it into bytes
      val bytes = serializer.toBinary(testEvent)
      logger.info(new String(bytes, ItemPushedSerializer.UTF8))

      val connection = EsConnection(system)
      val stream = EventStream("TestStream")

      val event = EventData("ItemPushed", data = Content(ByteString(bytes), ContentType.Json))

      val futureEvent = for {
        dummy <- connection.future(WriteEvents(stream, List(event)))
        //reading should happen after writing, otherwise the wrong event may be read
        ReadEventCompleted(event) <- connection.future(ReadEvent(stream, EventNumber.Last))
      } yield event
      val readEvent = Await.result(futureEvent, 2.seconds)

      val back = serializer.fromBinary(
        readEvent.data.data.value.toArray,
        None
      ).asInstanceOf[ItemPushed]

      back mustEqual testEvent
    }
  }

  "Persistent Actor" should {
    "work with EventStore" in {
      val actor = system.actorOf(Props[PersistentStackActor], "PersistentStack")
      actor ! PushItem("!")
      actor ! PushItem("world")
      actor ! Restart
      actor ! PrintState
      1 must_== 1
    }
  }

}