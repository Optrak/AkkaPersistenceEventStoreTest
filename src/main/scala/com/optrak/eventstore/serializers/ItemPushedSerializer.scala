package com.optrak.eventstore.serializers

import akka.persistence.eventstore.EventStoreSerializer
import akka.serialization.Serializer
import eventstore.ContentType
import java.nio.charset.Charset
import java.nio.ByteBuffer
import org.joda.time.{DateTimeZone, DateTime}
import com.optrak.eventstore.actors._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{read, write, formats}


object ItemPushedSerializer {
  val UTF8 = Charset.forName("UTF-8")
  val Identifier: Int = ByteBuffer.wrap("ItemPushed".getBytes(UTF8)).getInt

  //not sure if it's a bug, but org.json4s.ext.JodaTimeSerializers DateTime serializer loses milliseconds, thus
  //deserialized DateTime is not equal to the original one.
  class DateTimeSerializer extends CustomSerializer[DateTime](format => (
    {
      case JString(s) =>
        val timeZone = DateTimeZone.getDefault
        DateTime.parse(s).withZone(timeZone)
    },
    {
      case x: DateTime => JString(x.toString)
    }
    ))

  implicit val jFormats = formats(NoTypeHints) + new DateTimeSerializer
}


class ItemPushedSerializer extends EventStoreSerializer {
  import ItemPushedSerializer._
  import ItemPushedSerializer.jFormats

  def identifier = Identifier

  def includeManifest = true

  def fromBinary(bytes: Array[Byte], manifestOpt: Option[Class[_]]) = {
    implicit val manifest = manifestOpt match {
      case Some(x) => Manifest.classType[AnyRef](x)
      case None    => Manifest.AnyRef
    }
    read[ItemPushed](new String(bytes, UTF8))
  }

  def toBinary(o: AnyRef): Array[Byte] = {
    val event = o.asInstanceOf[ItemPushed]
    write(event).getBytes(UTF8)
  }

  def contentType = ContentType.Json

}