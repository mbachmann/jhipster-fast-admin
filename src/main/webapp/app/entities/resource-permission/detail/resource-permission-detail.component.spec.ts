import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResourcePermissionDetailComponent } from './resource-permission-detail.component';

describe('Component Tests', () => {
  describe('ResourcePermission Management Detail Component', () => {
    let comp: ResourcePermissionDetailComponent;
    let fixture: ComponentFixture<ResourcePermissionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResourcePermissionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ resourcePermission: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResourcePermissionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResourcePermissionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load resourcePermission on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resourcePermission).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
