package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewPipes;

/**
 * The {@code Pipes} class extends the {@code Component} class and provides
 * functionality to create and manage view components specific to pipes.
 * It overrides the base functionality to initialize and return a
 * {@code ViewPipes} object as its corresponding view component.
 */
public class Pipes extends Component {

    public Pipes() {
        super();
    }

    @Override
    public ViewComponent createViewComponent(){
        ViewPipes viewPipes = new ViewPipes();
        initializeViewComponent(viewPipes);
        return viewPipes;
    }

}