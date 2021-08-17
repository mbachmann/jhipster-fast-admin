import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionDetailComponent } from './permission-detail.component';

describe('Component Tests', () => {
  describe('Permission Management Detail Component', () => {
    let comp: PermissionDetailComponent;
    let fixture: ComponentFixture<PermissionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PermissionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ permission: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PermissionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PermissionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load permission on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.permission).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
