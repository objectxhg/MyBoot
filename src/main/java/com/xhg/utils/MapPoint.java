package com.xhg.utils;


import com.xhg.pojo.PointGeo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapPoint {

    /**
     *
     * @param point 目标经纬度
     * @param polygon 网格经纬度 网格坐标支持顺时针或者逆时针顺序
     * @return
     */
    public static boolean isPnpoly(PointGeo point, List<PointGeo> polygon){
        int h = polygon.size();
        boolean n = true;
        int j = 0;
        double g = 2e-10;
        PointGeo s, q;
        PointGeo e = point;
        s = polygon.get(0);
        for (int f = 1; f <= h; ++f) {
            if (e.equals(s)) {
                return n;
            }
            q = polygon.get((f % h));
            if (e.getLat() < Math.min(s.getLat(), q.getLat()) || e.getLat() > Math.max(s.getLat(), q.getLat())) {
                s = q;
                continue;
            }
            if (e.getLat() > Math.min(s.getLat(), q.getLat()) && e.getLat() < Math.max(s.getLat(), q.getLat())) {
                if (e.getLon() <= Math.max(s.getLon(), q.getLon())) {
                    if (s.getLat() ==(q.getLat())  && e.getLon() >= Math.min(s.getLon(), q.getLon())) {
                        return n;
                    }
                    if (s.getLon() ==(q.getLon()) ) {
                        if (s.getLon() ==(e.getLon()) ) {
                            return n;
                        } else {
                            ++j;

                        }
                    } else {
                        double r = (e.getLat() - s.getLat()) * (q.getLon() - s.getLon()) / (q.getLat() - s.getLat()) + s.getLon();

                        if (Math.abs(e.getLon() - r) < g) {
                            return n;
                        }
                        if (e.getLon() < r) {
                            ++j;

                        }
                    }
                }
            } else {
                if (e.getLat() ==(q.getLat())  && e.getLon() <= q.getLon()) {
                    PointGeo m = polygon.get(((f + 1) % h));
                    if (e.getLat() >= Math.min(s.getLat(), m.getLat()) && e.getLat() <= Math.max(s.getLat(), m.getLat())) {
                        ++j;

                    } else {
                        j += 2;
                    }
                }
            }
            s = q;
        }
        if (j % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }


}