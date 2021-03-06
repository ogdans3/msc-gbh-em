package core.btree.tasks.executable;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import core.btree.Blackboard;
import core.btree.tasks.TaskTickTracker;
import core.simulation.hla.HlaManager;
import core.unit.Unit;
import no.ffi.hlalib.interactions.HLAinteractionRoot.LBMLMessage.LBMLTask.WaitInteraction;


public class WaitTaskExec extends LeafTask<Blackboard<Unit>> {

    private final TaskTickTracker taskTickTracker = new TaskTickTracker(1);

    @Override
    public Task.Status execute() {
        if (taskTickTracker.getCurrentStatus() == TaskTickTracker.Status.FIRST) {
            sendLLBMLWaitTask();
        }
        taskTickTracker.tick();
        if (taskTickTracker.getCurrentStatus() == TaskTickTracker.Status.DONE) {
            return Status.SUCCEEDED;
        }
        return Task.Status.RUNNING;
    }

    @Override
    public void start() {
        super.start();
        taskTickTracker.reset();
    }

    private void sendLLBMLWaitTask(){
        WaitInteraction interaction = new WaitInteraction();
        interaction.setTaskee(getObject().getUnit().getMarking());

        HlaManager.getInstance().sendInteraction(interaction);
    }

    @Override
    protected Task<Blackboard<Unit>> copyTo(Task<Blackboard<Unit>> task) {
        return task;
    }
}
