import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogServiceFaComponent } from './list/catalog-service-fa.component';
import { CatalogServiceFaDetailComponent } from './detail/catalog-service-fa-detail.component';
import { CatalogServiceFaUpdateComponent } from './update/catalog-service-fa-update.component';
import { CatalogServiceFaDeleteDialogComponent } from './delete/catalog-service-fa-delete-dialog.component';
import { CatalogServiceFaRoutingModule } from './route/catalog-service-fa-routing.module';

@NgModule({
  imports: [SharedModule, CatalogServiceFaRoutingModule],
  declarations: [
    CatalogServiceFaComponent,
    CatalogServiceFaDetailComponent,
    CatalogServiceFaUpdateComponent,
    CatalogServiceFaDeleteDialogComponent,
  ],
  entryComponents: [CatalogServiceFaDeleteDialogComponent],
})
export class CatalogServiceFaModule {}
