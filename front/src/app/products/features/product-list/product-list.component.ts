import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from "@angular/core";
import { FormsModule } from '@angular/forms';
import { Product } from "app/products/data-access/product.model";
import { ProductsService } from "app/products/data-access/products.service";
import { ProductFormComponent } from "app/products/ui/product-form/product-form.component";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import { BadgeModule } from 'primeng/badge';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { take, tap } from 'rxjs';

const emptyProduct: Product = {
  id: 0,
  code: "",
  name: "",
  description: "",
  image: "",
  category: "",
  price: 0,
  quantity: 0,
  internalReference: "",
  shellId: 0,
  inventoryStatus: "INSTOCK",
  rating: 0,
  createdAt: 0,
  updatedAt: 0,
};

@Component({
  selector: "app-product-list",
  templateUrl: "./product-list.component.html",
  styleUrls: ["./product-list.component.scss"],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DataViewModule,
    CardModule,
    ButtonModule,
    DialogModule,
    ProductFormComponent,
    BadgeModule,
    DropdownModule,
    InputTextModule
  ],
})
export class ProductListComponent implements OnInit {
  private readonly productsService = inject(ProductsService);

  private readonly cart = this.productsService.cart;

  public readonly products = this.productsService.products;

  public productsToDisplay: Product[] = [];
  public paginatedProducts: Product[] = [];

  public first = 0;
  public rows = 5;
  public totalRecords = 0;

  public searchText = '';
  public selectedCategory: string | null = null;
  public selectedStatus: string | null = null;
  public categories: string[] = [];
  public statuses = [
    { label: 'En stock', value: 'INSTOCK' },
    { label: 'Stock faible', value: 'LOWSTOCK' },
    { label: 'Rupture de stock', value: 'OUTOFSTOCK' }
  ];

  public isDialogVisible = false;
  public isCreation = false;
  public isCartVisible = false;
  public readonly editedProduct = signal<Product>(emptyProduct);

  ngOnInit() {
    this.productsService.get().pipe(take(1),
      tap(products => {
        this.productsToDisplay = products;
        this.categories = [...new Set(products.map(p => p.category))];
        this.totalRecords = products.length;
        this.updatePaginatedProducts();
      })).subscribe();
  }

  public onCreate() {
    this.isCreation = true;
    this.isDialogVisible = true;
    this.editedProduct.set(emptyProduct);
  }

  public onUpdate(product: Product) {
    this.isCreation = false;
    this.isDialogVisible = true;
    this.editedProduct.set(product);
  }

  public onDelete(product: Product) {
    this.productsService.delete(product.id).subscribe();
  }

  public onSave(product: Product) {
    if (this.isCreation) {
      this.productsService.create(product).subscribe();
    } else {
      this.productsService.update(product).subscribe();
    }
    this.closeDialog();
  }

  public onCancel() {
    this.closeDialog();
  }

  public onAddToCart(product: Product) {
    this.productsService.addToCart(product.id, 1);
  }

  public onRemoveFromCart(product: Product) {
    this.productsService.removeFromCart(product.id, 1);
  }

  public getProductCartQuantity(product: Product): number {
    return this.cart().items.find(item => item.product.id === product.id)?.quantity || 0;
  }

  public onCart() {
    this.isCartVisible = !this.isCartVisible;
    this.productsToDisplay = this.isCartVisible ? this.getCartProducts() : this.products();
    this.totalRecords = this.productsToDisplay.length;
    this.first = 0;
    this.updatePaginatedProducts();
  }

  public getCartProducts(): Product[] {
    return this.cart().items.map(item => item.product);
  }

  public onPageChange(event: { first: number; rows: number }) {
    this.first = event.first;
    this.rows = event.rows;
    this.updatePaginatedProducts();
  }

  public onFilter() {
    this.first = 0;
    const filteredProducts = this.products().filter(product => {
      const matchesSearch = !this.searchText || 
        product.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        product.description.toLowerCase().includes(this.searchText.toLowerCase()) ||
        product.code.toLowerCase().includes(this.searchText.toLowerCase());

      const matchesCategory = !this.selectedCategory || 
        product.category === this.selectedCategory;

      const matchesStatus = !this.selectedStatus || 
        product.inventoryStatus === this.selectedStatus;

      return matchesSearch && matchesCategory && matchesStatus;
    });

    this.productsToDisplay = filteredProducts;
    this.totalRecords = filteredProducts.length;
    this.updatePaginatedProducts();
  }

  public clearFilters() {
    this.searchText = '';
    this.selectedCategory = null;
    this.selectedStatus = null;
    this.onFilter();
  }

  private updatePaginatedProducts() {
    this.paginatedProducts = this.productsToDisplay.slice(this.first, this.first + this.rows);
  }

  private closeDialog() {
    this.isDialogVisible = false;
  }
}
