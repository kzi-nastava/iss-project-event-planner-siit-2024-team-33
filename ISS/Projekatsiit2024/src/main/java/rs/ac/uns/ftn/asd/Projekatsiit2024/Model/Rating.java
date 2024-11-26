package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int ID;
    public int Value;
    public String Comment;
    public Boolean Accepted;

    @ManyToOne
    public AuthentifiedUser Author;
    @ManyToOne
    public Offer Offer;
}
