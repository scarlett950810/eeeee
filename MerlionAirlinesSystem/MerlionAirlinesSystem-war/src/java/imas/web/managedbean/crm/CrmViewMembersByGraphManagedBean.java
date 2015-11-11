/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.crm;

import imas.crm.entity.MemberEntity;
import imas.crm.sessionbean.CustomerAccountManagementSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author wutong
 */
@Named(value = "crmViewMembersByGraphManagedBean")
@ViewScoped
public class CrmViewMembersByGraphManagedBean implements Serializable{

    @EJB
    private CustomerAccountManagementSessionBeanLocal customerAccountManagementSessionBeanLocal;
    private List<MemberEntity> memberList;
    private List<String> countryList;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    private PieChartModel pieModel3;
    private PieChartModel pieModel4;
    /**
     * Creates a new instance of CrmViewMembersByGraphManagedBean
     */
    public CrmViewMembersByGraphManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        memberList = customerAccountManagementSessionBeanLocal.retrieveMembers();
        countryList = new ArrayList<>();
        if(!memberList.isEmpty()){
            for(MemberEntity m: memberList){
                String nationality = m.getNationality();
                Boolean check = true;
                if(!countryList.isEmpty()){
                    for(String n: countryList){
                        if(nationality.equals(n)){
                            check = false;
                            break;
                        }
                    }
                }
                if(check) countryList.add(nationality);
            }
        }
        createPieModel1();
        createPieModel2();
        createPieModel3();
        createPieModel4();
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    public PieChartModel getPieModel3() {
        return pieModel3;
    }

    public void setPieModel3(PieChartModel pieModel3) {
        this.pieModel3 = pieModel3;
    }

    public PieChartModel getPieModel4() {
        return pieModel4;
    }

    public void setPieModel4(PieChartModel pieModel4) {
        this.pieModel4 = pieModel4;
    }
    
    private void createPieModel1() {
        pieModel1 = new PieChartModel();
        
        for(String s: countryList){
            int count = 0;
            for(MemberEntity m: memberList){
                if(m.getNationality().equals(s))
                    count++;
            }
            pieModel1.set(s, count);
        }        
         
        pieModel1.setTitle("Members by Geography");
        pieModel1.setLegendPosition("w");
    }
    
    private void createPieModel2(){
        pieModel2 = new PieChartModel();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        int count5 = 0;
        
        for(MemberEntity m: memberList){
            if(m.getMileage()<=2000)
                count1++;
            else if(m.getMileage()<=4000)
                count2++;
            else if(m.getMileage()<=6000)
                count3++;
            else if(m.getMileage()<=8000)
                count4++;
            else
                count5++;
        }
        
        pieModel2.set("0-2000 Miles", count1);
        pieModel2.set("2000-4000 Miles", count2);
        pieModel2.set("4000-6000 Miles", count3);
        pieModel2.set("6000-8000 Miles", count4);
        pieModel2.set("8000-10000 Miles", count5);
        
        pieModel2.setTitle("Members by Mileage");
        pieModel2.setLegendPosition("w");
    }
    
    private void createPieModel3(){
        pieModel3 = new PieChartModel();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        int count5 = 0;
        int count6 = 0;
        int count7 = 0;
        
        
        for(MemberEntity m: memberList){
            int age = m.getAge();
            if(age<=20)
                count1++;
            else if(age<=30)
                count2++;
            else if(age<=40)
                count3++;
            else if(age<=50)
                count4++;
            else if(age<=60)
                count5++;
            else if(age<=70)
                count6++;
            else
                count7++;
        }
        
        pieModel3.set("0-20", count1);
        pieModel3.set("20-30", count2);
        pieModel3.set("30-40", count3);
        pieModel3.set("40-50", count4);
        pieModel3.set("50-60", count5);
        pieModel3.set("60-70", count6);
        pieModel3.set("Older than 70", count7);
        
        pieModel3.setTitle("Members by Age");
        pieModel3.setLegendPosition("w");
    }
    
    private void createPieModel4(){
        pieModel4 = new PieChartModel();
        int count1 = 0;
        int count2 = 0;
        
        for(MemberEntity m: memberList){
            if(m.getGender().equals("Male"))
                count1++;
            else
                count2++;
        }
        
        pieModel4.set("Male Members", count1);
        pieModel4.set("Female Members", count2);
        
        pieModel4.setTitle("Members by Gender");
        pieModel4.setLegendPosition("w");
    }
    
}
