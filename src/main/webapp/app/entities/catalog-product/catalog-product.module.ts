import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogProductComponent } from './list/catalog-product.component';
import { CatalogProductDetailComponent } from './detail/catalog-product-detail.component';
import { CatalogProductUpdateComponent } from './update/catalog-product-update.component';
import { CatalogProductDeleteDialogComponent } from './delete/catalog-product-delete-dialog.component';
import { CatalogProductRoutingModule } from './route/catalog-product-routing.module';

@NgModule({
  imports: [SharedModule, CatalogProductRoutingModule],
  declarations: [
    CatalogProductComponent,
    CatalogProductDetailComponent,
    CatalogProductUpdateComponent,
    CatalogProductDeleteDialogComponent,
  ],
  entryComponents: [CatalogProductDeleteDialogComponent],
})
export class CatalogProductModule {}
