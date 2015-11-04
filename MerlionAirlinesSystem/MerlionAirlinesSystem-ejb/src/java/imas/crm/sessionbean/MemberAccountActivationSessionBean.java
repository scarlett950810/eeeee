/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.common.entity.StaffEntity;
import imas.crm.entity.MemberEntity;
import static java.lang.Boolean.TRUE;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class MemberAccountActivationSessionBean implements MemberAccountActivationSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public MemberEntity getMemberBasedOnToken(String token) {
        Query query = entityManager.createQuery("SELECT m FROM MemberEntity m WHERE m.token = :token");
        query.setParameter("token", token);

        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();
        if(members.isEmpty()){
            return null;
        }else{
            return members.get(0);
        }
        
    }

    @Override
    public void activateMemberAccount(MemberEntity member) {
        member.setActivationStatus(TRUE);
        entityManager.merge(member);
    }

    
    
    

    
}
