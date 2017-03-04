package com.taintech.sxa

import org.scalatest.prop._
import org.scalatest.{Matchers, PropSpec}

class AppSpec
  extends PropSpec
  with GeneratorDrivenPropertyChecks
  with Matchers {

  property ("Addition and multiplication are related") {
    forAll { (x: Int) =>
      whenever(x > 0) {
        x * 2 should be(x + x)
      }
    }
  }

}
