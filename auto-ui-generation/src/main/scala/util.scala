package util

def fromCamelCase(s: String): String =
  val sb = StringBuilder()
  val length = s.length
  var currIndex = 0
  var prevWasLower = false
  var wasBoundary = false
  while currIndex < length do
    val curr = s.charAt(currIndex)
    val nextIndex = currIndex + 1
    val nextIsLower = (nextIndex < length) && s.charAt(nextIndex).isLower
    val isBoundary = curr.isUpper && (prevWasLower || nextIsLower)
    val isDelimitation = isBoundary && !wasBoundary
    val out =
      if currIndex == 0 then curr.toUpper
      else if isDelimitation then ' '
      else if isBoundary && nextIsLower then curr.toLower
      else curr
    sb.append(out)
    prevWasLower = curr.isLower
    wasBoundary = isBoundary
    if !isDelimitation then currIndex = nextIndex
  end while
  sb.toString
end fromCamelCase


