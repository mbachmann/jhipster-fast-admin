import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoleFaDetailComponent } from './role-fa-detail.component';

describe('Component Tests', () => {
  describe('RoleFa Management Detail Component', () => {
    let comp: RoleFaDetailComponent;
    let fixture: ComponentFixture<RoleFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RoleFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ role: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RoleFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoleFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load role on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.role).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
