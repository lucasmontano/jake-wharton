package com.lucasmontano.jakewharton.data

import com.lucasmontano.jakewharton.BuildConfig

/**
 * Pagination Data
 *
 * Please check @see <a href="https://www.w3.org/wiki/LinkHeader">W3:LinkHeader</a> for more details about LinkHeader.
 */
data class LinkHeaderData(private val linkHeader: String) {

    var first: String = BuildConfig.JAKE_URL
    var last: String? = null
    var next: String? = null
    var prev: String? = null

    init {
        val links = linkHeader.split(",")

        for (link in links) {

            val segments = link.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            if (segments.size < 2) continue

            var linkPart = segments[0].trim({ it <= ' ' })
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) continue

            linkPart = linkPart.substring(1, linkPart.length - 1)

            for (i in 1 until segments.size) {

                val rel = segments[i].trim({ it <= ' ' }).split("=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                if (rel.size < 2 || "rel" != rel[0]) continue

                var relValue = rel[1]
                if (relValue.startsWith("\"") && relValue.endsWith("\"")) {
                    relValue = relValue.substring(1, relValue.length - 1)
                }

                when (relValue) {
                    "first" -> first = linkPart
                    "last" -> last = linkPart
                    "next" -> next = linkPart
                    "prev" -> prev = linkPart
                }
            }
        }
    }
}