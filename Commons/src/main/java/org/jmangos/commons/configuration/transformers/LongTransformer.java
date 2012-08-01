/*******************************************************************************
 * Copyright (C) 2012 JMaNGOS <http://jmangos.org/>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.jmangos.commons.configuration.transformers;

import java.lang.reflect.Field;

import org.jmangos.commons.configuration.PropertyTransformer;
import org.jmangos.commons.configuration.TransformationException;



// TODO: Auto-generated Javadoc
/**
 * Transforms value that represents long to long. Value can be in decimal or hex format.
 */
public class LongTransformer implements PropertyTransformer<Long>
{
	/**
	 * Shared instance of this transformer. It's thread-safe so no need of multiple instances
	 */
	public static final LongTransformer	SHARED_INSTANCE	= new LongTransformer();

	/**
	 * Transforms value to long.
	 *
	 * @param value value that will be transformed
	 * @param field value will be assigned to this field
	 * @return Long that represents value
	 * @throws TransformationException if something went wrong
	 */
	@Override
	public Long transform(String value, Field field) throws TransformationException
	{
		try
		{
			return Long.decode(value);
		}
		catch (Exception e)
		{
			throw new TransformationException(e);
		}
	}
}
