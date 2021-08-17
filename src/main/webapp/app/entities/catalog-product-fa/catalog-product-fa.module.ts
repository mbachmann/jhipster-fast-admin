import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogProductFaComponent } from './list/catalog-product-fa.component';
import { CatalogProductFaDetailComponent } from './detail/catalog-product-fa-detail.component';
import { CatalogProductFaUpdateComponent } from './update/catalog-product-fa-update.component';
import { CatalogProductFaDeleteDialogComponent } from './delete/catalog-product-fa-delete-dialog.component';
import { CatalogProductFaRoutingModule } from './route/catalog-product-fa-routing.module';

@NgModule({
  imports: [SharedModule, CatalogProductFaRoutingModule],
  declarations: [
    CatalogProductFaComponent,
    CatalogProductFaDetailComponent,
    CatalogProductFaUpdateComponent,
    CatalogProductFaDeleteDialogComponent,
  ],
  entryComponents: [CatalogProductFaDeleteDialogComponent],
})
export class CatalogProductFaModule {}
