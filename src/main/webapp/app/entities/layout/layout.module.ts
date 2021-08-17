import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LayoutComponent } from './list/layout.component';
import { LayoutDetailComponent } from './detail/layout-detail.component';
import { LayoutUpdateComponent } from './update/layout-update.component';
import { LayoutDeleteDialogComponent } from './delete/layout-delete-dialog.component';
import { LayoutRoutingModule } from './route/layout-routing.module';

@NgModule({
  imports: [SharedModule, LayoutRoutingModule],
  declarations: [LayoutComponent, LayoutDetailComponent, LayoutUpdateComponent, LayoutDeleteDialogComponent],
  entryComponents: [LayoutDeleteDialogComponent],
})
export class LayoutModule {}
