from fastapi import FastAPI

from app.answer_service import AnswerService
from app.models import AnswerRequest, AnswerResponse
from app.policy_service import PolicyService


app = FastAPI(
    title="Marlabs Employee Support Python Service",
    version="0.1.0"
)

policy_service = PolicyService()
answer_service = AnswerService(policy_service)


@app.get("/health")
def health():
    return {
        "status": "UP",
        "service": "python-service"
    }


@app.post("/internal/answer", response_model=AnswerResponse)
def internal_answer(request: AnswerRequest) -> AnswerResponse:

    result = answer_service.answer(
        tenant=request.tenant,
        role=request.role,
        question=request.question,
        as_of=request.as_of
    )

    return AnswerResponse(**result)