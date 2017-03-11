package com.taintech.sxa

import scala.collection.mutable
import scala.xml.{Node, NodeSeq}

/**
  * Author: Rinat Tainov 
  * Date: 05/03/2017
  */
class XMLStats(xml: Node) {
  lazy val tags: Set[String] = {
    val tagSet: mutable.Set[String] = mutable.Set.empty
    def traverse(node: NodeSeq, tagSet: mutable.Set[String]): mutable.Set[String] = node.headOption match {
      case None => mutable.Set.empty
      case Some(node: Node) =>
        tagSet+=node.label
        traverse(node.nonEmptyChildren, tagSet)
    }
    traverse(xml, tagSet)
    tagSet.toSet
  }
}
