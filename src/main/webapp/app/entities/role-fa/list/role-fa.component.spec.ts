jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoleFaService } from '../service/role-fa.service';

import { RoleFaComponent } from './role-fa.component';

describe('Component Tests', () => {
  describe('RoleFa Management Component', () => {
    let comp: RoleFaComponent;
    let fixture: ComponentFixture<RoleFaComponent>;
    let service: RoleFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoleFaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(RoleFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoleFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RoleFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.roles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
