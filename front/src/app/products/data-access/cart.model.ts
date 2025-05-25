import { Product } from "./product.model";
export interface Cart {
    items: CartItem[];
}

interface CartItem {
    product: Product;
    quantity: number;
}
