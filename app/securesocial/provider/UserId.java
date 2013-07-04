/**
 * Copyright 2011 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package securesocial.provider;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

/**
 * A class to uniquely identify users. This combines the id the user has on an
 * external service (eg: twitter, facebook) with the provider type.
 */
public class UserId implements java.io.Serializable {
	/**
	 * The id the user has in a external service.
	 */
	public String id;

	/**
	 * The provider this user belongs to.
	 */
	public ProviderType provider;

	/**
	 * 
	 */
	public boolean equals(Object otherOb) {
		if (this == otherOb) {
			return true;
		}
		if (!(otherOb instanceof UserId)) {
			return false;
		}
		UserId other = (UserId) otherOb;
		return ((id == null ? other.id == null : id.equals(other.id)) && (provider == null ? other.provider == null
				: provider.equals(other.provider)));
	}

	/**
	 * 
	 */
	public int hashCode() {
		return ((id == null ? 0 : id.hashCode()) ^ ((provider == null ? 0
				: provider.hashCode())));
	}
}
