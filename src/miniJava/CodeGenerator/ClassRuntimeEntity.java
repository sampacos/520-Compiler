package miniJava.CodeGenerator;

public class ClassRuntimeEntity extends RuntimeEntityDescriptor {

	public ClassRuntimeEntity(int size) {
		super(size);
		this.classDisplacement = -1;
		this.isCreated = false;
		// TODO Auto-generated constructor stub
	}
	
	int classDisplacement;
	boolean isCreated;
	
}
