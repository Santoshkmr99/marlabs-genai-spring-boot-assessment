import json
from datetime import date
from pathlib import Path


class PolicyService:

    def __init__(self, policy_file: str = "data/policies.json"):
        self.policy_file = Path(policy_file)
        self.policies = self._load_policies()

    def _load_policies(self) -> list[dict]:
        with self.policy_file.open("r", encoding="utf-8") as file:
            return json.load(file)

    def find_eligible_policies(
        self,
        tenant: str,
        role: str,
        as_of: date
    ) -> list[dict]:

        eligible = []

        for policy in self.policies:

            if policy["approval_state"] != "Approved":
                continue

            if policy["tenant"] != tenant:
                continue

            if policy["role"] != role:
                continue

            effective_from = date.fromisoformat(
                policy["effective_from"]
            )

            effective_to = date.fromisoformat(
                policy["effective_to"]
            )

            if effective_from <= as_of < effective_to:
                eligible.append(policy)

        return eligible