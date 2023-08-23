
package com.te.zealhr.audit;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class EntityAuditorAware extends BaseConfigController implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		return Optional.of(getUserId());
	}

}
