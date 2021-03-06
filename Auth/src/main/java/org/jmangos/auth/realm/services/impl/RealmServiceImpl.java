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
package org.jmangos.auth.realm.services.impl;

import java.util.List;

import org.jmangos.auth.dao.RealmDao;
import org.jmangos.auth.entities.RealmEntity;
import org.jmangos.auth.realm.services.RealmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("realmService")
public class RealmServiceImpl implements RealmService {

    @Autowired
    private RealmDao realmDao;

    @Override
    public RealmEntity readRealm(final Long id) {

        return this.realmDao.findOne(id);
    }

    @Override
    public List<RealmEntity> readRealms() {

        return this.realmDao.findAll();
    }

    @Override
    public RealmEntity createOrUpdateRealm(final RealmEntity realmEntity) {

        return this.realmDao.save(realmEntity);
    }

    @Override
    public void deleteRealm(final RealmEntity realmEntity) {

        this.realmDao.delete(realmEntity);
    }

}
