import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { catchError, Observable, of, tap } from "rxjs";
import { Cart } from "./cart.model";
import { Product } from "./product.model";

@Injectable({
    providedIn: "root"
}) export class ProductsService {

    private readonly http = inject(HttpClient);
    private readonly path = "/api/products";

    private readonly _products = signal<Product[]>([]);

    public readonly products = this._products.asReadonly();

    private readonly _cart = signal<Cart>({ items: [] });

    public readonly cart = this._cart.asReadonly();


    public get(): Observable<Product[]> {
        return this.http.get<Product[]>(this.path).pipe(
            catchError((error) => {
                return this.http.get<Product[]>("assets/products.json");
            }),
            tap((products) => this._products.set(products)),
        );
    }

    public create(product: Product): Observable<boolean> {
        return this.http.post<boolean>(this.path, product).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => [product, ...products])),
        );
    }

    public update(product: Product): Observable<boolean> {
        return this.http.patch<boolean>(`${this.path}/${product.id}`, product).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => {
                return products.map(p => p.id === product.id ? product : p)
            })),
        );
    }

    public delete(productId: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.path}/${productId}`).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => products.filter(product => product.id !== productId))),
        );
    }

    public addToCart(productId: number, quantity: number): void {
        const product = this._products().find(p => p.id === productId);
        if (product) {
            product.quantity = product.quantity - quantity < 0 ? 0 : product.quantity - quantity;
            const currentCart = this._cart();
            const existingItem = currentCart.items.find(item => item.product.id === productId);

            if (existingItem) {
                this._cart.update(cart => ({
                    ...cart,
                    items: cart.items.map(item => item.product.id === productId ?
                        { ...item, quantity: item.quantity + quantity } : item)
                }));
            } else {
                this._cart.update(cart => ({
                    ...cart,
                    items: [...cart.items, { product, quantity }]
                }));
            }
        }
    }

    public removeFromCart(productId: number, quantity: number): void {
        const product = this._products().find(p => p.id === productId);
        if (product) {
            product.quantity = product.quantity + quantity;
            this._cart.update(cart => {
                const updatedItems = cart.items.map(item => {
                    if (item.product.id === productId) {
                        const newQuantity = item.quantity - quantity;
                        return newQuantity <= 0 ? null : { ...item, quantity: newQuantity };
                    }
                    return item;
                }).filter((item): item is Cart['items'][0] => item !== null);

                return {
                    ...cart,
                    items: updatedItems
                };
            });
        }
    }
}
