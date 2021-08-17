import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogUnitFaComponent } from './list/catalog-unit-fa.component';
import { CatalogUnitFaDetailComponent } from './detail/catalog-unit-fa-detail.component';
import { CatalogUnitFaUpdateComponent } from './update/catalog-unit-fa-update.component';
import { CatalogUnitFaDeleteDialogComponent } from './delete/catalog-unit-fa-delete-dialog.component';
import { CatalogUnitFaRoutingModule } from './route/catalog-unit-fa-routing.module';

@NgModule({
  imports: [SharedModule, CatalogUnitFaRoutingModule],
  declarations: [CatalogUnitFaComponent, CatalogUnitFaDetailComponent, CatalogUnitFaUpdateComponent, CatalogUnitFaDeleteDialogComponent],
  entryComponents: [CatalogUnitFaDeleteDialogComponent],
})
export class CatalogUnitFaModule {}
