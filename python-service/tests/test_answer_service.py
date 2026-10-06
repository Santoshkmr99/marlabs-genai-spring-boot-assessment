from app.answer_service import AnswerService
from app.policy_service import PolicyService


def create_service():
    policy_service = PolicyService()
    return AnswerService(policy_service)


def test_certification_returns_answered():
    service = create_service()

    result = service.answer(
        tenant="Atlas",
        role="employee",
        question="What is my annual certification reimbursement limit?",
        as_of="2026-09-21"
    )

    assert result["status"] == "ANSWERED"
    assert result["answer"] == (
        "The annual certification reimbursement limit for employees "
        "is INR 25000."
    )

    assert result["citations"] == [
        {
            "chunk_id": "atlas-cert-current",
            "quote": (
                "The annual certification reimbursement limit for "
                "employees is INR 25000."
            )
        }
    ]


def test_home_office_returns_conflict():
    service = create_service()

    result = service.answer(
        tenant="Atlas",
        role="employee",
        question="What is my annual home-office allowance?",
        as_of="2026-09-21"
    )

    assert result["status"] == "CONFLICT"
    assert result["answer"] is None

    citation_ids = {
        citation["chunk_id"]
        for citation in result["citations"]
    }

    assert citation_ids == {
        "atlas-home-office-a",
        "atlas-home-office-b"
    }


def test_unknown_question_returns_insufficient_evidence():
    service = create_service()

    result = service.answer(
        tenant="Atlas",
        role="employee",
        question="What is my annual gym membership allowance?",
        as_of="2026-09-21"
    )

    assert result["status"] == "INSUFFICIENT_EVIDENCE"
    assert result["answer"] is None
    assert result["citations"] == []


def test_contractor_gets_contractor_certification_policy():
    service = create_service()

    result = service.answer(
        tenant="Atlas",
        role="contractor",
        question="What is my annual certification reimbursement limit?",
        as_of="2026-09-21"
    )

    assert result["status"] == "ANSWERED"
    assert result["citations"][0]["chunk_id"] == "atlas-cert-contractor"


def test_boreal_cannot_use_atlas_certification_policy():
    service = create_service()

    result = service.answer(
        tenant="Boreal",
        role="employee",
        question="What is my annual certification reimbursement limit?",
        as_of="2026-09-21"
    )

    assert result["status"] == "ANSWERED"
    assert result["citations"][0]["chunk_id"] == "boreal-cert-current"