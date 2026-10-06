from datetime import date

from app.policy_service import PolicyService


class AnswerService:

    def __init__(self, policy_service: PolicyService):
        self.policy_service = policy_service

    def answer(
        self,
        tenant: str,
        role: str,
        question: str,
        as_of: str
    ) -> dict:

        requested_date = date.fromisoformat(as_of)

        policies = self.policy_service.find_eligible_policies(
            tenant=tenant,
            role=role,
            as_of=requested_date
        )

        relevant_policies = self._find_relevant_policies(
            policies,
            question
        )

        if not relevant_policies:
            return {
                "status": "INSUFFICIENT_EVIDENCE",
                "answer": None,
                "citations": []
            }

        if self._has_conflict(relevant_policies):
            return {
                "status": "CONFLICT",
                "answer": None,
                "citations": [
                    {
                        "chunk_id": policy["id"],
                        "quote": policy["text"]
                    }
                    for policy in relevant_policies
                ]
            }

        policy = relevant_policies[0]

        return {
            "status": "ANSWERED",
            "answer": policy["text"],
            "citations": [
                {
                    "chunk_id": policy["id"],
                    "quote": policy["text"]
                }
            ]
        }

    def _find_relevant_policies(
        self,
        policies: list[dict],
        question: str
    ) -> list[dict]:

        question_lower = question.lower()

        benefit_keyword = None

        if "certification" in question_lower:
            benefit_keyword = "cert"

        elif "home-office" in question_lower or "home office" in question_lower:
            benefit_keyword = "home-office"

        elif "travel" in question_lower:
            benefit_keyword = "travel"

        elif "training" in question_lower:
            benefit_keyword = "training"

        if not benefit_keyword:
            return []

        return [
            policy
            for policy in policies
            if benefit_keyword in policy["id"]
        ]

    def _has_conflict(self, policies: list[dict]) -> bool:

        if len(policies) <= 1:
            return False

        texts = {
            policy["text"]
            for policy in policies
        }

        return len(texts) > 1