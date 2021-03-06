/*******************************************************************************
 * Copyright (C) 2013 JMaNGOS <http://jmangos.org/>
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
 ******************************************************************************/
/**
 *
 */
package org.jmangos.tools.dbc.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jmangos.commons.entities.ChrClasses;
import org.jmangos.commons.enums.Classes;
import org.jmangos.tools.dbc.service.AbstractDbcService;
import org.jmangos.tools.dbc.struct.ChrClassesEntry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author MinimaJack
 */
@Service
@Qualifier(value = "chrClassesService")
public class ChrClassesService extends AbstractDbcService<ChrClasses, ChrClassesEntry> {

    ChrClassesService() {

        super();
    }

    @PersistenceContext(unitName = "world")
    private EntityManager entityManager;

    @Override
    public void save(final ChrClasses chrClasses) {

        if (chrClasses.getId() == null) {
            this.entityManager.persist(chrClasses);
        } else {
            this.entityManager.merge(chrClasses);
        }

    }

    @Override
    public void saveAll() {

        this.entityManager.flush();
        ChrClassesEntry entry = getEntry();
        do {
            final ChrClasses ce = new ChrClasses();
            ce.setId(Classes.get(entry.ClassID.get()));
            ce.setName(entry.name.get());
            ce.setCinematicSequence(entry.CinematicSequence.get());
            ce.setPowerType(entry.powerType.get());
            ce.setExpansion(entry.expansion.get());
            ce.setType(entry.type.get());
            this.entityManager.persist(ce);
            if ((entry.getCurrposition() % 10000) == 0) {
                this.entityManager.flush();
                this.entityManager.clear();
            }
        } while ((entry = entry.next()) != null);
        this.entityManager.flush();
        this.entityManager.clear();
    }

    @Override
    public String getDbcPath() {

        return "./../Realm/dbc/ChrClasses.dbc";
    }

}
