package com.titan.ynsjy.mview;

import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.RouteTask;

/**
 * Created by li on 2017/5/9.
 */

public interface INavigatView extends IBaseView{

    NAFeaturesAsFeature getmStops();

    RouteTask getRouteTask();


}
