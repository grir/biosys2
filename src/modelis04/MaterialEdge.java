/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelis04;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author i
 */
public class MaterialEdge {
   public int nBoxes;// [0] - left boundary, [nBoxes+1] - right b.
   
   //public enum InitCases {ZZZZ, AZZZ, AAZZ, AZBZ, AABZ, AABB};
   public  ArrayList<Molecule> mols;  // 
   
   /////////////////////////////////////////////////////////////
   public MaterialEdge(int nBoxes){ 
         this.nBoxes = nBoxes; 
         this.mols =  new ArrayList<>();
   }
   /////////////////////////////////////////////////////////////
   public ArrayList<Integer> getAtX(int x){
      ArrayList<Integer> res = new ArrayList<> ();
      for(int i=0;i<mols.size();i++)
         if(mols.get(i).x == x) res.add(i);
      return res;   
   }
   /////////////////////////////////////////////////////////////
   public int getNumberAtX(int x){
        int xx=x;
        if(x <= -1) xx=0;
        if(x >= nBoxes) xx = nBoxes;
      
        int res = 0;      
        for (Molecule mol : mols) {
           if (mol.x == xx) {
               res++;
           }
        }
      
        return res;   
   }
   /////////////////////////////////////////////////////////////
   public void setLeftBound(int n){
      if (mols.isEmpty())   {
          for(int i=0;i<(n+2);i++)
             mols.add(new Molecule(0));
          return;   
      }
      int k = n - getNumberAtX(0);
        
      for(int i = 0;i < k;i++)
          mols.add(new Molecule(0));
   }
   
   /////////////////////////////////////////////////////////////
   public void setRightBound(int n){
      if (mols.isEmpty())   {
          for(int i=0;i<(n+2);i++)
             mols.add(new Molecule(nBoxes));
          return;   
      }
      int k = n - getNumberAtX(nBoxes);
        
      for(int i = 0;i < k;i++)
          mols.add(new Molecule(nBoxes));
   }
   //////////////////////////////////////////////////////////////
   public void randomJump(Random generator){
      int idx = generator.nextInt(mols.size());
      Molecule mol = mols.get(idx);
      int x = mol.x;   
      double nm1 = getNumberAtX(x-1);
      double n0 = getNumberAtX(x);
      double np1 = getNumberAtX(x+1);
      //double pm1 = hyster(nm1-n0);
      double pm1;
      if(nm1>n0) pm1=0;
      else pm1 =  1 - Math.pow(1.0/3, (n0-nm1));

      double pp1;
      if(np1>n0) pp1=0;
      else pp1 =  1 - Math.pow(1.0/3, (n0-np1));
      
      double p0 = 1.0/3;//hyster(0);      
      //double pp1 = hyster(np1-n0);
      double sm = pm1+p0+pp1;
      
      mol.pLeft = pm1/sm; 
      mol.pRight = pp1/sm;
      //System.out.println(""+pm1/sm+" "+p0/sm+" "+pp1/sm);
      mol.jump(generator.nextDouble());
   }
   /////////////////////////////////////////////////////////////////
   public void randomReaction(Random generator, MaterialEdge other, 
                              double reactionProb){
      int idx = generator.nextInt(mols.size());
      Molecule mol = mols.get(idx);
      int x = mol.x;   
      ArrayList<Integer> molOther;
      molOther = other.getAtX(x);
      
      for (Integer molother : molOther) {
           if (generator.nextDouble() < reactionProb) {
               mols.remove(idx);
               other.mols.remove(molother);
               break;
           } else {
           }
       }
   }
   
   //////////////////////////////////////////////////////      
   public static double hyster(double a){
        return Math.atan(a)/Math.PI + 0.5;
   }    
}
