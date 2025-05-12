package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewPipes;

public class Pipes extends Component {

    public Pipes() {
        super();
    }

    public ViewComponent createViewComponent(){
        ViewPipes viewPipes = new ViewPipes();
        initializeViewComponent(viewPipes);
        return viewPipes;
    }

}