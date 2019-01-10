package si.matjazcerkvenik.dtools.web.webhook;

public class World {
	
	private String name;
    private Long id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	@Override
	public String toString() {
		return "World [name=" + name + ", id=" + id + "]";
	}
    
    
	
}
