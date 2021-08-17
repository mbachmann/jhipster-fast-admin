import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogUnitComponent } from './list/catalog-unit.component';
import { CatalogUnitDetailComponent } from './detail/catalog-unit-detail.component';
import { CatalogUnitUpdateComponent } from './update/catalog-unit-update.component';
import { CatalogUnitDeleteDialogComponent } from './delete/catalog-unit-delete-dialog.component';
import { CatalogUnitRoutingModule } from './route/catalog-unit-routing.module';

@NgModule({
  imports: [SharedModule, CatalogUnitRoutingModule],
  declarations: [CatalogUnitComponent, CatalogUnitDetailComponent, CatalogUnitUpdateComponent, CatalogUnitDeleteDialogComponent],
  entryComponents: [CatalogUnitDeleteDialogComponent],
})
export class CatalogUnitModule {}
