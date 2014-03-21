package scalax.chart
package module

/** $MarkingInfo */
object Marking extends Marking

/** $MarkingInfo
  *
  * @define MarkingInfo [[Marking]] contains enrichments concerning the handling of markers in plots.
  *
  * {{{
  * val data = Seq((0,0),(1,1),(2,2)).toXYSeriesCollection("some data")
  * val chart = XYLineChart(data)
  *
  * chart.plot.domain.markers += 1
  *
  * chart.show()
  * }}}
  */
trait Marking extends Imports {

  /** Converts some type `A` to a marker. */
  trait ToMarker[A] {
    type X <: Marker
    def toMarker(a: A): X
  }

  /** Contains default [[ToMarker]] instances. */
  object ToMarker {
    @inline final def apply[A](implicit TM: ToMarker[A]): ToMarker[A] = TM

    implicit def MarkerToMarker[A <: Marker]: ToMarker[A] = new ToMarker[A] {
      type X = A
      def toMarker(a: A): X = a
    }

    implicit def NumericToMarker[A: Numeric]: ToMarker[A] = new ToMarker[A] {
      type X = ValueMarker
      def toMarker(a: A): X = new ValueMarker(implicitly[Numeric[A]].toDouble(a))
    }

    implicit def NumericTupleToMarker[A: Numeric, B: Numeric]: ToMarker[(A,B)] = new ToMarker[(A,B)] {
      type X = IntervalMarker
      def toMarker(t: (A,B)): X = {
        val start = implicitly[Numeric[A]].toDouble(t._1)
        val end   = implicitly[Numeric[B]].toDouble(t._2)

        new IntervalMarker(start, end)
      }
    }
  }

  /** Converts some type `A` to a category marker. */
  trait ToCategoryMarker[A] {
    type X <: CategoryMarker
    def toMarker(a: A): X
  }

  /** Contains default [[ToCategoryMarker]] instances. */
  object ToCategoryMarker {
    @inline final def apply[A](implicit TM: ToCategoryMarker[A]): ToCategoryMarker[A] = TM

    implicit def MarkerToCategoryMarker[A <: CategoryMarker]: ToCategoryMarker[A] = new ToCategoryMarker[A] {
      type X = A
      def toMarker(a: A): X = a
    }

    implicit def ComparableToCategoryMarker[A <% Comparable[A]]: ToCategoryMarker[A] = new ToCategoryMarker[A] {
      type X = CategoryMarker
      def toMarker(a: A): X = new CategoryMarker(a)
    }
  }

}
