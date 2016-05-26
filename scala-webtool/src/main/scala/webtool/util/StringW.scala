package webtool.util

import scala.util.control.Exception.catching

final class StringW(val underlying: String) extends AnyVal {
  /**
   * @return true if a string is null, empty or contains only whitespace
   */
  def isBlank = StringUtil.isBlank(underlying)

  /**
   * @return true if string contains non-whitespace characters
   */
  def nonBlank = !isBlank

  /**
   * Replaces word barriers with underscores and converts entire string to lowercase.
   * "name".toSnakeCase == "name"
   * "NAME".toSnakeCase == "name"
   * "EpisodeId".toSnakeCase == "episode_id"
   * "beamLYstuff".toSnakeCase == "beam_ly_stuff"
   * "BEAMlySTUFF.toSnakeCase "beam_ly_stuff"
   * @return string with word barriers represented with underscores
   */
  def toSnakeCase: String = replaceWordBoundary("_")

  /**
   * Replaces word barriers with hyphens (and by "hyphens" what is actually meant is "hyphen-minus", ie. U+002D) and
   * converts entire string to lowercase.
   * {{{
   * "name".toHyphenCase == "name"
   * "NAME".toHyphenCase == "name"
   * "EpisodeId".toHyphenCase == "episode-id"
   * "beamLYstuff".toHyphenCase == "beam-ly-stuff"
   * "BEAMlySTUFF.toHyphenCase "beam-ly-stuff"
   * }}}
   * @return string with word barriers represented with hyphens
   */
  def toHyphenCase: String = replaceWordBoundary("-")

  /**
   * Replaces word barriers with provided string and converts entire string to lowercase.
   * {{{
   * "name".replaceWordBoundary("|") == "name"
   * "NAME".replaceWordBoundary("|") == "name"
   * "EpisodeId".replaceWordBoundary("|") == "episode|id"
   * "beamLYstuff".replaceWordBoundary("|") == "beam|ly|stuff"
   * "BEAMlySTUFF.replaceWordBoundary("|") "beam|ly|stuff"
   * }}}
   * @return string with word barriers represented with hyphens
   */
  private def replaceWordBoundary(replacementString: String): String = {
    underlying
      .replaceAll("([A-Z]+)([A-Z])([a-z]+)", "$1$2" + replacementString + "$3")
      .replaceAll("([a-z]+)([A-Z]+)", "$1" + replacementString + "$2")
      .toLowerCase
  }

  def toBooleanOption = catching(classOf[IllegalArgumentException]) opt underlying.toBoolean
  def toByteOption = catching(classOf[NumberFormatException]) opt underlying.toByte
  def toShortOption = catching(classOf[NumberFormatException]) opt underlying.toShort
  def toIntOption = catching(classOf[NumberFormatException]) opt underlying.toInt
  def toLongOption = catching(classOf[NumberFormatException]) opt underlying.toLong
  def toFloatOption = catching(classOf[NumberFormatException]) opt underlying.toFloat
  def toDoubleOption = catching(classOf[NumberFormatException]) opt underlying.toDouble

  def splitByNewLine(src: String): Array[String] = {
    src.split("\n")
    //src.split(TextUtil.LineSeparator)
  }

  def indexOfNonWhitespace: Int = {
    require(underlying != null)
    underlying.find { !Character.isWhitespace(_) } match {
      case Some(a) => a
      case _       => -1
    }
  }
}

object StringW {
  def isBlank(str: String): Boolean = new StringW(str).isBlank
  def apply(str: String): StringW = new StringW(str)
}