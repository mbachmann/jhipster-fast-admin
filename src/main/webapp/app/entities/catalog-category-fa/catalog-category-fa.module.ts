import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogCategoryFaComponent } from './list/catalog-category-fa.component';
import { CatalogCategoryFaDetailComponent } from './detail/catalog-category-fa-detail.component';
import { CatalogCategoryFaUpdateComponent } from './update/catalog-category-fa-update.component';
import { CatalogCategoryFaDeleteDialogComponent } from './delete/catalog-category-fa-delete-dialog.component';
import { CatalogCategoryFaRoutingModule } from './route/catalog-category-fa-routing.module';

@NgModule({
  imports: [SharedModule, CatalogCategoryFaRoutingModule],
  declarations: [
    CatalogCategoryFaComponent,
    CatalogCategoryFaDetailComponent,
    CatalogCategoryFaUpdateComponent,
    CatalogCategoryFaDeleteDialogComponent,
  ],
  entryComponents: [CatalogCategoryFaDeleteDialogComponent],
})
export class CatalogCategoryFaModule {}
