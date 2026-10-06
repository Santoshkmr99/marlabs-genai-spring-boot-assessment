from typing import Literal

from pydantic import BaseModel, Field


class AnswerRequest(BaseModel):
    caller_id: str
    tenant: str
    role: str
    question: str = Field(min_length=1)
    as_of: str


class Citation(BaseModel):
    chunk_id: str
    quote: str


class AnswerResponse(BaseModel):
    status: Literal[
        "ANSWERED",
        "INSUFFICIENT_EVIDENCE",
        "CONFLICT"
    ]
    answer: str | None
    citations: list[Citation]