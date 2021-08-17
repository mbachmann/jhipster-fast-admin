import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogCategoryComponent } from './list/catalog-category.component';
import { CatalogCategoryDetailComponent } from './detail/catalog-category-detail.component';
import { CatalogCategoryUpdateComponent } from './update/catalog-category-update.component';
import { CatalogCategoryDeleteDialogComponent } from './delete/catalog-category-delete-dialog.component';
import { CatalogCategoryRoutingModule } from './route/catalog-category-routing.module';

@NgModule({
  imports: [SharedModule, CatalogCategoryRoutingModule],
  declarations: [
    CatalogCategoryComponent,
    CatalogCategoryDetailComponent,
    CatalogCategoryUpdateComponent,
    CatalogCategoryDeleteDialogComponent,
  ],
  entryComponents: [CatalogCategoryDeleteDialogComponent],
})
export class CatalogCategoryModule {}
