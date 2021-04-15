package com.urlshortener.dal.util

import javax.persistence.TypedQuery

fun <T> TypedQuery<T>.singleResult(): T? = this.resultList.firstOrNull()