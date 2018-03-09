package com.lucasmontano.jakewharton.data

/**
 * Pagination Data
 *
 * Please check @see <a href="https://www.w3.org/wiki/LinkHeader">W3:LinkHeader</a> for more details about LinkHeader.
 */
data class LinkHeaderData(val next: String, val last: String, val first: String, val prev: String)