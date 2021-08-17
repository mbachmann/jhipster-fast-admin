import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogServiceComponent } from './list/catalog-service.component';
import { CatalogServiceDetailComponent } from './detail/catalog-service-detail.component';
import { CatalogServiceUpdateComponent } from './update/catalog-service-update.component';
import { CatalogServiceDeleteDialogComponent } from './delete/catalog-service-delete-dialog.component';
import { CatalogServiceRoutingModule } from './route/catalog-service-routing.module';

@NgModule({
  imports: [SharedModule, CatalogServiceRoutingModule],
  declarations: [
    CatalogServiceComponent,
    CatalogServiceDetailComponent,
    CatalogServiceUpdateComponent,
    CatalogServiceDeleteDialogComponent,
  ],
  entryComponents: [CatalogServiceDeleteDialogComponent],
})
export class CatalogServiceModule {}
