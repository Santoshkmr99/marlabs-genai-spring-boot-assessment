from datetime import date

from app.policy_service import PolicyService


def test_loads_all_policies():
    service = PolicyService()

    assert len(service.policies) == 12


def test_atlas_employee_gets_current_certification_policy():
    service = PolicyService()

    policies = service.find_eligible_policies(
        tenant="Atlas",
        role="employee",
        as_of=date(2026, 9, 21)
    )

    policy_ids = {policy["id"] for policy in policies}

    assert "atlas-cert-current" in policy_ids
    assert "atlas-cert-historical" not in policy_ids
    assert "atlas-cert-future" not in policy_ids
    assert "atlas-cert-draft" not in policy_ids

def test_boreal_policy_not_available_to_atlas_employee():
    service = PolicyService()

    policies = service.find_eligible_policies(
        tenant="Atlas",
        role="employee",
        as_of=date(2026, 9, 21)
    )

    policy_ids = {policy["id"] for policy in policies}

    assert "boreal-cert-current" not in policy_ids
    assert "boreal-home-office-current" not in policy_ids

def test_contractor_only_gets_contractor_policy():
    service = PolicyService()

    policies = service.find_eligible_policies(
        tenant="Atlas",
        role="contractor",
        as_of=date(2026, 9, 21)
    )

    policy_ids = {policy["id"] for policy in policies}

    assert "atlas-cert-contractor" in policy_ids
    assert "atlas-cert-current" not in policy_ids