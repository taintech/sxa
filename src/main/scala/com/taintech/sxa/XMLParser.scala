package com.taintech.sxa

import javax.xml.parsers.SAXParserFactory

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * Author: Rinat Tainov 
  * Date: 11/03/2017
  */
class XMLParser extends DefaultHandler {

  private val uris = mutable.Map[String, Int]()
  private val localNames = mutable.Map[String, Int]()
  private val qNames = mutable.Map[String, Int]()
  private val attributesMap = mutable.Map[Attribute, Int]()

  override def startElement(uri: String, localName: String, qName: String, attributes: Attributes): Unit = {
    if (uri.nonEmpty) incrementMap(uri, uris)
    if (localName.nonEmpty) incrementMap(localName, localNames)
    if (qName.nonEmpty) incrementMap(qName, qNames)
    if (attributes.getLength!=0) extractAttributes(attributes).foreach(v => incrementMap(v, attributesMap))
  }

  private def extractAttributes(attributes: Attributes): List[Attribute] ={
    @tailrec
    def iterate(attributes: Attributes, i: Int, n: Int, list: List[Attribute]): List[Attribute] =
      if (i == n) list
      else {
        iterate(attributes, i + 1, n, Attribute(attributes.getQName(i), attributes.getValue(i)) :: list)
      }
    iterate(attributes, 0, attributes.getLength, Nil)
  }

  private def incrementMap[T](key: T, map: mutable.Map[T, Int]): Unit =
    map.get(key) match {
      case None => map+=(key -> 1)
      case Some(n) => map+=(key -> (n+1))
    }

  def stats: XMLStats = XMLStats(uris.toMap, localNames.toMap, qNames.toMap, attributesMap.toMap)
}

object XMLParser {
  def apply(file: String): XMLStats = {
    val factory = SAXParserFactory.newInstance()
    val saxParser = factory.newSAXParser()
    val statParser = new XMLParser()
    saxParser.parse(file, statParser)
    statParser.stats
  }
}

case class Attribute(qName: String, value: String)
case class XMLStats(uris: Map[String, Int],
                    localNames: Map[String, Int],
                    qNames: Map[String, Int],
                    attributes: Map[Attribute, Int])
