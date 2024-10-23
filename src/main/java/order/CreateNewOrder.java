package order;

public class CreateNewOrder {

    private String[] ingredients;

    public CreateNewOrder(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public CreateNewOrder() {
    }


    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

}