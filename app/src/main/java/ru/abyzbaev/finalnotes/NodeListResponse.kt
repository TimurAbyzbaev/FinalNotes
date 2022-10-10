package ru.abyzbaev.finalnotes

import ru.abyzbaev.finalnotes.NodeListSource

interface NodeListResponse {
    fun initialized(nodeListSource: NodeListSource?)
}