package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.UnverifiedUserUpgrade;

@Getter
public class UpgradeRequest extends UpgradeUser {
	private Integer id;
	
	public UpgradeRequest(UnverifiedUserUpgrade uuu) {
		this.id = uuu.getId();
		this.setResidency(uuu.getResidency());
		this.setPhoneNumber(uuu.getPhoneNumber());
		this.setDescription(uuu.getDescription());
		this.setProviderName(uuu.getProviderName());
		this.setRole(uuu.getRole().getName());
	}
}
