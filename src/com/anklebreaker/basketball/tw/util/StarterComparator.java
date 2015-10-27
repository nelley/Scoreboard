package com.anklebreaker.basketball.tw.util;

import java.util.Comparator;

import com.anklebreaker.basketball.tw.recordboard.Player;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;

public class StarterComparator implements Comparator<Player>{
    @Override
    public int compare(Player lhs, Player rhs) {
        // starter will arranged in the head of the list
        //return rhs.getIsStarter().compareTo(lhs.getIsStarter());
        int c;
        // compare by isStarter first
        c = rhs.getIsStarter().compareTo(lhs.getIsStarter());
        if (c == 0){
            // compare by isPlayer then
            c = lhs.getIsBench().compareTo(rhs.getIsBench());
        }
        return c;
    }

}
