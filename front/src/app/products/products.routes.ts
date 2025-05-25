import { Routes } from "@angular/router";
import { ContactComponent } from "../contact/features/contact/contact.component";
import { ProductListComponent } from "./features/product-list/product-list.component";

export const PRODUCTS_ROUTES: Routes = [
	{
		path: "list",
		component: ProductListComponent,
	},
	{
		path: "contact",
		component: ContactComponent,
	},
	{ path: "**", redirectTo: "list" },
];
