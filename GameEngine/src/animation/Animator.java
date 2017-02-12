package animation;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import animatedModel.AnimatedModel;
import animatedModel.Joint;
import renderEngine.DisplayManager;


public class Animator {

	private final AnimatedModel entity;

	private float animationTime = 0;
	private Animation currentAnimation;


	public Animator(AnimatedModel entity) {
		this.entity = entity;
	}


	public void doAnimation(Animation animation) {
		//this.animationTime = 0;
		this.currentAnimation = animation;
	}

	public void update() {
		if (currentAnimation == null) {
			return;
		}
		increaseAnimationTime();
		Map<String, Matrix4f> currentPose = calculateCurrentAnimationPose();
		applyPoseToJoints(currentPose, entity.getRootJoint(), new Matrix4f());

	}


	private void increaseAnimationTime() {
		animationTime += DisplayManager.getFrameTimeSeconds();
		if (animationTime > currentAnimation.getLength()) {
			this.animationTime %= currentAnimation.getLength();
		}
	}

	private Map<String, Matrix4f> calculateCurrentAnimationPose() {
		KeyFrame[] frames = getPreviousAndNextFrames();
		float progression = calculateProgression(frames[0], frames[1]);
		return interpolatePoses(frames[0], frames[1], progression);
	}


	private void applyPoseToJoints(Map<String, Matrix4f> currentPose, Joint joint, Matrix4f parentTransform) {
		Matrix4f currentLocalTransform = currentPose.get(joint.name);
		Matrix4f currentTransform = Matrix4f.mul(parentTransform, currentLocalTransform, null);
		for (Joint childJoint : joint.children) {
			applyPoseToJoints(currentPose, childJoint, currentTransform);
		}
		Matrix4f.mul(currentTransform, joint.getInverseBindTransform(), currentTransform);
		joint.setAnimationTransform(currentTransform);
	}


	private KeyFrame[] getPreviousAndNextFrames() {
		KeyFrame previousFrame = null;
		KeyFrame nextFrame = null;
		for (KeyFrame frame : currentAnimation.getKeyFrames()) {
			if (frame.getTimeStamp() > animationTime) {
				nextFrame = frame;
				break;
			}
			previousFrame = frame;
		}
		previousFrame = previousFrame == null ? nextFrame : previousFrame;
		nextFrame = nextFrame == null ? previousFrame : nextFrame;
		return new KeyFrame[] { previousFrame, nextFrame };
	}


	private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame) {
		float timeDifference = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
		return (animationTime - previousFrame.getTimeStamp()) / timeDifference;
	}


	private Map<String, Matrix4f> interpolatePoses(KeyFrame previousFrame, KeyFrame nextFrame, float progression) {
		Map<String, Matrix4f> currentPose = new HashMap<String, Matrix4f>();
		for (String jointName : previousFrame.getJointKeyFrames().keySet()) {
			JointTransform previousTransform = previousFrame.getJointKeyFrames().get(jointName);
			JointTransform nextTransform = nextFrame.getJointKeyFrames().get(jointName);
			JointTransform currentTransform = JointTransform.interpolate(previousTransform, nextTransform, progression);
			currentPose.put(jointName, currentTransform.getLocalTransform());
		}
		return currentPose;
	}

}
